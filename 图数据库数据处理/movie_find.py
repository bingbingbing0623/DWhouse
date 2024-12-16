import pandas as pd

# 读取原始 CSV 文件
df = pd.read_csv('Final.csv')

# 处理评论总数，可以通过 ASIN 计数
# 先计算每个 ASIN 的评论数量
comment_counts = df['ASIN'].value_counts()

# 计算每个 ASIN 的整体评分（假设取 "Rated" 列的平均值）
# 首先去除缺失的评分数据，然后按 ASIN 分组计算平均评分
average_ratings = df.groupby('ASIN')['score'].mean()

# 提取唯一的 ASIN、Title、Release date 和 Genre
# 使用 drop_duplicates 来确保每个 ASIN 唯一
unique_movies = df[['ASIN', 'title', 'Release date', 'Genres']].drop_duplicates()

# 将评论总数和平均评分添加到唯一电影数据中
unique_movies['comment_count'] = unique_movies['ASIN'].map(comment_counts)
unique_movies['average_rating'] = unique_movies['ASIN'].map(average_ratings)

# 将结果保存到新的 CSV 文件
unique_movies.to_csv('movies.csv', index=False)

# 输出新的 CSV 内容（可以选择是否输出）
print(unique_movies)
