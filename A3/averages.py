file = open('output.csv', 'r')

i = 0
averages = dict()
for line in file:
    if i == 0:
        i += 1
        continue
    line = line.strip()
    line_array = line.split(',')
    key = (line_array[0], line_array[1])
    if key in averages:
        averages[key]['time_req'] += int(line_array[3])
        if line_array[4] != 'NA':
            averages[key]['highest_num_c_sat'] += int(line_array[4])
        averages[key]['nodes_expanded'] += int(line_array[5])
        averages[key]['count'] += 1
    else:
        averages[key] = dict() 
        averages[key]['time_req'] = int(line_array[3])
        if line_array[4] != 'NA':
            averages[key]['highest_num_c_sat'] = int(line_array[4])
        else:
            averages[key]['highest_num_c_sat'] = line_array[4]
        averages[key]['nodes_expanded'] = int(line_array[5])
        averages[key]['count'] = 1
final_avg = dict()
for k in averages:
    total_num = averages[k]['count']
    final_avg[k] = dict()
    final_avg[k]['time_req'] = int(averages[k]['time_req']) / total_num
    if averages[k]['highest_num_c_sat'] != 'NA':
        final_avg[k]['highest_num_c_sat'] = int(averages[k]['highest_num_c_sat']) / total_num
    else:
        final_avg[k]['highest_num_c_sat'] = 'NA'
    final_avg[k]['nodes_expanded'] = int(averages[k]['nodes_expanded']) / total_num

file.close()

out = open('averages_out.csv', 'w')

out.write('algo,file_name,sol,time_required,highest_num_c_sat,nodes_expanded\n')
for k in final_avg:
    out.write(k[0] + ',')
    out.write(k[1] + ',')
    if 's' in [1]:
        out.write('SAT,')
    else:
        out.write('UNSAT,')
    out.write(str(final_avg[k]['time_req']) + ',')
    out.write(str(final_avg[k]['highest_num_c_sat']) + ',')
    out.write(str(final_avg[k]['nodes_expanded']) + ',')
    out.write('\n')
    
out.close()
