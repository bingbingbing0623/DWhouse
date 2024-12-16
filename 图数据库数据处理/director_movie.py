import pandas as pd

# 读取原始 CSV 文件
df = pd.read_csv('Final.csv')

# 读取 director.csv 文件，获取导演名字到 director_id 的映射
director_df = pd.read_csv('director.csv')

# 将导演的名字和 director_id 对应起来，方便查找
director_dict = dict(zip(director_df['director_name'], director_df['director_id']))

# 初始化一个列表，用于存储新的 CSV 数据
director_data = []

# 遍历每一行，处理导演信息
for index, row in df.iterrows():
    asin = row['ASIN']
    director = row['Director']

    # 如果是 NaN 或空值，跳过该行
    if pd.isna(director):
        continue

    # 确保 `director` 是字符串类型
    director = str(director)

    # 分割导演名字，假设名字之间是由逗号隔开的
    director_names = director.split(', ')

    # 遍历每个导演并分配 director_id
    for di in director_names:
        if di in director_dict:
            director_id = director_dict[di]
        else:
            continue

        # 将导演数据添加到列表
        director_data.append([asin, director_id])

# 创建新的 DataFrame，用于保存 ASIN 和 director_id
director_df_new = pd.DataFrame(director_data, columns=['ASIN', 'director_id'])

# 删除重复的 (ASIN, director_id) 配对
director_df_new = director_df_new.drop_duplicates(subset=['ASIN', 'director_id'], keep='first')

# 将去重后的结果保存到新的 CSV 文件
director_df_new.to_csv('asin_director_mapping.csv', index=False)

# 输出 director_df_new 文件的内容
print(director_df_new)
