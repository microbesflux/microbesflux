def check_user_upload_file_format(f):
    if not f:
        return False
    header = f.readline().split(",")
    if header[0].lower() != "time":
        return False# it has to be time
    if header[1].lower() != "biomass":
        return False
    length = len(header)
    current = -1.0
    for lines in f:
        try:
            numbers = map(float, lines.split(","))
        except:
            return False
        if len(numbers) != length:
            return False
        if numbers[0] <= current:
            return False
        current = numbers[0]
    return True

import sys
if __name__=="__main__":
    fi = sys.argv[1]
    f = open(fi, 'r')
    print check_user_upload_file_format(f)
    f.close()
