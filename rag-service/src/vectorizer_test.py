from text_vectorizer import TextVectorizer

def test_search():
    """ 测试：向量化输入文本，查询并输出相似的知识库文本 """
    # 初始化向量器
    vectorizer = TextVectorizer('config/config.json')
    
    # 加载已存在的索引
    vectorizer.load_index("data/store_knowledge.index")
    
    # 测试查询
    test_queries = [
        "如何退换货？",
        "有什么支付方式？",
        "运动鞋尺码怎么选择？"
    ]
    
    for query in test_queries:
        print(f"\n搜索查询: {query}")
        print("-" * 50)
        results = vectorizer.search_similar(query, k=3)
        for id, text, distance in results:
            print(f"ID: {id}")
            print(f"相似度距离: {distance:.4f}")
            print(f"文本内容: {text}")
            print("-" * 50)

if __name__ == "__main__":
    test_search()
