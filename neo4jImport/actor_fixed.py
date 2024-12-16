import csv

input_file = 'actor.csv'
output_file = 'actor_fixed.csv'

with open(input_file, 'r', encoding='utf-8') as infile, open(output_file, 'w', encoding='utf-8', newline='') as outfile:
    reader = csv.reader(infile)
    writer = csv.writer(outfile)
    
    for row in reader:
        fixed_row = []
        for cell in row:
            # 修复引号问题: 替换两个双引号为一个双引号
            fixed_cell = cell.replace('""', '"')
            fixed_row.append(fixed_cell)
        writer.writerow(fixed_row)

print(f"Fixed CSV file saved as {output_file}")
