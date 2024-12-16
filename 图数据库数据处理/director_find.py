import pandas as pd

# 读取原始 CSV 文件
df = pd.read_csv('Final.csv')

# 初始化字典，用于保存导演名字到 director_id 的映射
director_dict = {}

# 当前的 director_id 从 1 开始
director_id_counter = 1

# 遍历每一行，处理导演信息
for directors in df['Director']:
    # 如果是 NaN 或空值，跳过该行
    if pd.isna(directors):
        continue

    # 确保 `directors` 是字符串类型
    directors = str(directors)

    # 分割导演名字，假设名字之间是由逗号隔开的
    director_names = directors.split(', ')
    
    for director in director_names:
        # 如果导演名字还没有在字典中，则添加它
        if director not in director_dict:
            director_dict[director] = director_id_counter
            director_id_counter += 1

# 创建新的 DataFrame，用于保存 director_id 和 director_name
director_df = pd.DataFrame(
    list(director_dict.items()), 
    columns=['director_name', 'director_id']
)

# 调整列顺序，将 director_id 放在前面
director_df = director_df[['director_id', 'director_name']]

# 将 director_df 写入新的 CSV 文件
director_df.to_csv('director.csv', index=False)

# 输出 director.csv 文件的内容
print(director_df)
