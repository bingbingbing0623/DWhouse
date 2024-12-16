import csv

input_file = 'actor_fixed.csv'
output_file = 'actor_fixed_clean.csv'

def fix_quotes(cell):
    # 如果字段内容包含引号并且没有正确闭合，尝试修复它
    if cell.count('"') % 2 != 0:  # 检查引号的数量是否是偶数
        cell = cell + '"'  # 如果奇数个引号，补充一个引号
    # 替换两个双引号为一个双引号
    return cell.replace('""', '"')

with open(input_file, 'r', encoding='utf-8') as infile, open(output_file, 'w', encoding='utf-8', newline='') as outfile:
    reader = csv.reader(infile)
    writer = csv.writer(outfile)
    
    for row in reader:
        fixed_row = [fix_quotes(cell) for cell in row]
        writer.writerow(fixed_row)

print(f"Fixed CSV file saved as {output_file}")
