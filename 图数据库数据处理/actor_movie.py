import pandas as pd

# 读取原始 CSV 文件
df = pd.read_csv('Final.csv')

# 读取 actor.csv 文件，获取演员名字到 actor_id 的映射
actor_df = pd.read_csv('actor.csv')

# 将演员的名字和 actor_id 对应起来，方便查找
actor_dict = dict(zip(actor_df['actor_name'], actor_df['actor_id']))

# 初始化一个列表，用于存储新的 CSV 数据
actor_data = []

# 遍历每一行，处理演员信息
for index, row in df.iterrows():
    asin = row['ASIN']
    actors = row['Actors']
    main_actor = row.get('main_actor', '')  # 获取主演员，默认为空字符串

    # 如果是 NaN 或空值，跳过该行
    if pd.isna(actors):
        continue

    # 确保 `actors` 是字符串类型
    actors = str(actors)

    # 分割演员名字，假设名字之间是由逗号隔开的
    actor_names = actors.split(', ')

    # 处理 main_actor 字段，如果有多个主演员，用逗号分隔
    if main_actor and isinstance(main_actor, str):  # 确保 main_actor 是字符串
        main_actor_names = main_actor.split(', ')  # 处理多个主演员
    else:
        main_actor_names = []

    # 遍历每个演员并分配 actor_id，同时标记主演员
    for actor in actor_names:
        # 如果演员名字在 actor_dict 中，则获取对应的 actor_id
        if actor in actor_dict:
            actor_id = actor_dict[actor]
        else:
            # 如果演员不在字典中，则跳过
            continue

        # 标记是否是主演员，检查是否在 main_actor_names 中
        is_main_actor = 1 if actor in main_actor_names else 0

        # 添加到 actor_data 列表
        actor_data.append([asin, actor_id, is_main_actor])

# 创建新的 DataFrame，用于保存 ASIN, actor_id 和 is_main_actor
actor_df_new = pd.DataFrame(actor_data, columns=['ASIN', 'actor_id', 'is_main_actor'])

# 删除重复的 (ASIN, actor_id) 配对，只保留 is_main_actor 为 1 的记录，或者保留任意一条
actor_df_new = actor_df_new.sort_values(by=['ASIN', 'actor_id', 'is_main_actor'], ascending=[True, True, False])
actor_df_new = actor_df_new.drop_duplicates(subset=['ASIN', 'actor_id'], keep='first')

# 将去重后的结果保存到新的 CSV 文件
actor_df_new.to_csv('asin_actor_mapping.csv', index=False)

# 输出 actor_df_new 文件的内容
print(actor_df_new)
