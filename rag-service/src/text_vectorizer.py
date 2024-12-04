import pandas as pd
import numpy as np
from openai import OpenAI
import faiss
import json
from typing import List, Tuple, Dict, Any
import time
from dotenv import load_dotenv
import os
from peewee import *
from datetime import datetime

# 首先创建一个数据库实例
db = DatabaseProxy()  # 使用代理，这样我们可以在运行时设置实际的数据库

# 定义数据库模型
class AIContext(Model):
    id = AutoField()
    text = TextField()
    created_at = DateTimeField(default=datetime.now)
    
    class Meta:
        database = db
        table_name = 'ai_context'

class DatabaseManager:
    def __init__(self, db_config: Dict[str, Any]):
        self.database = MySQLDatabase(
            db_config['database'],
            user=db_config['user'],
            password=db_config['password'],
            host=db_config['host']
        )
        # 设置实际的数据库连接
        db.initialize(self.database)
        
    def connect(self) -> None:
        """连接数据库"""
        if self.database.is_closed():
            self.database.connect()
        
    def close(self) -> None:
        """关闭数据库连接"""
        if not self.database.is_closed():
            self.database.close()
            
    def create_tables(self) -> None:
        """创建数据库表"""
        self.connect()
        self.database.create_tables([AIContext], safe=True)
            
    def drop_tables(self) -> None:
        """删除数据库表"""
        self.connect()
        self.database.drop_tables([AIContext], safe=True)

class TextVectorizer:
    def __init__(self, config_path: str):
        """使用配置文件初始化"""
        self.config = self._load_config(config_path)
        self.client = OpenAI(api_key=self.config['openai']['api_key'])
        self.dimension = 1536
        self.index = faiss.IndexFlatL2(self.dimension)
        self.db_manager = DatabaseManager(self.config['mysql'])
        
    @staticmethod
    def _load_config(config_path: str) -> Dict[str, Any]:
        """加载配置文件"""
        with open(config_path, 'r') as f:
            return json.load(f)

    def setup_database(self) -> None:
        """初始化数据库"""
        try:
            self.db_manager.drop_tables()
            self.db_manager.create_tables()
        except Exception as e:
            print(f"Database setup error: {e}")
            raise

    def insert_texts_from_file(self, file_path: str) -> "List[Tuple[int, str]]":
        """从文本文件读取数据并插入到MySQL"""
        inserted_records = []
        self.db_manager.connect()
        
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                for line in f:
                    text = line.strip()
                    if text:
                        record = AIContext.create(text=text)
                        inserted_records.append((record.id, text))
        finally:
            self.db_manager.close()
                    
        return inserted_records

    def get_embeddings(self, texts: "List[str]") -> "List[List[float]]":
        """获取文本嵌入向量"""
        try:
            response = self.client.embeddings.create(
                model="text-embedding-ada-002",
                input=texts
            )
            return [item.embedding for item in response.data]
        except Exception as e:
            print(f"Error getting embeddings: {e}")
            return []

    def process_and_store(self, text_records: "List[Tuple[int, str]]", batch_size: int = 100) -> None:
        """批次处理文本并按ID顺序存储向量"""
        for i in range(0, len(text_records), batch_size):
            batch = text_records[i:i + batch_size]
            texts = [record[1] for record in batch]
            
            embeddings = self.get_embeddings(texts)
            if not embeddings:
                continue

            vectors = np.array(embeddings).astype('float32')
            self.index.add(vectors)
            
            print(f"Processed batch of {len(batch)} items")
            time.sleep(1) # 避免OpenAI API限制

    def save_index(self, index_path: str) -> None:
        """保存FAISS索引"""
        faiss.write_index(self.index, index_path)

    def load_index(self, index_path: str) -> None:
        """加载FAISS索引"""
        self.index = faiss.read_index(index_path)
        
    def search_similar(self, query: str, k: int = 5) -> "List[Tuple[int, str, float]]":
        """搜索相似文本"""
        query_embedding = self.get_embeddings([query])[0]
        query_vector = np.array([query_embedding]).astype('float32')
        
        distances, indices = self.index.search(query_vector, k)
        
        results = []
        self.db_manager.connect()
        try:
            for idx, distance in zip(indices[0], distances[0]):
                mysql_id = int(idx) + 1
                try:
                    record = AIContext.get_by_id(mysql_id)
                    results.append((record.id, record.text, float(distance)))
                except AIContext.DoesNotExist:
                    continue
        finally:
            self.db_manager.close()
                
        return results

# 进行向量化和索引构建
if __name__ == "__main__":
    # 初始化向量化器
    vectorizer = TextVectorizer('config/config.json')
    
    try:
        # 设置数据库
        vectorizer.setup_database()
        
        # 从文本文件读取并插入数据
        text_records = vectorizer.insert_texts_from_file("data/运动鞋店铺知识库.txt")
        
        # 处理文本并存储向量
        vectorizer.process_and_store(text_records, batch_size=10)
        
        # 保存FAISS索引
        vectorizer.save_index("data/store_knowledge.index")
    except Exception as e:
        print(f"An error occurred: {e}")
