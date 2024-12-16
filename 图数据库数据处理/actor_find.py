import pandas as pd

# 读取原始 CSV 文件
df = pd.read_csv('Final.csv')

# 初始化一个字典，用于保存演员名字到 actor_id 的映射
actor_dict = {}

# 当前的 actor_id 从 1 开始
actor_id_counter = 1

# 遍历每一行，处理演员信息
for actors in df['Actors']:
    # 如果是 NaN 或空值，跳过该行
    if pd.isna(actors):
        continue

    # 确保 `actors` 是字符串类型
    actors = str(actors)

    # 分割演员名字，假设名字之间是由逗号隔开的
    actor_names = actors.split(', ')
    
    for actor in actor_names:
        # 如果演员名字还没有在字典中，则添加它
        if actor not in actor_dict:
            actor_dict[actor] = actor_id_counter
            actor_id_counter += 1

# 创建新的 DataFrame，用于保存 actor_id 和 actor_name
actor_df = pd.DataFrame(
    list(actor_dict.items()), 
    columns=['actor_name', 'actor_id']
)

# 调整列顺序，将 actor_id 放在前面
actor_df = actor_df[['actor_id', 'actor_name']]

# 将 actor_df 写入新的 CSV 文件
actor_df.to_csv('actor.csv', index=False)

