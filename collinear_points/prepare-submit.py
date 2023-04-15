from os import listdir, path 
from shutil import copymode
from sys import argv

def copy_files_and_remove_package(src_dir, dest_dir):
    for filename in listdir(src_dir):
        src_path = path.join(src_dir, filename)
        dest_path = path.join(dest_dir, filename)

        if path.isfile(src_path):
            with open(src_path, 'r') as f_in, open(dest_path, 'w') as f_out:
                for line in f_in:
                    if 'package' not in line:
                        f_out.write(line)
            copymode(src_path, dest_path)

if __name__ == "__main__":
    _, src_dir, dest_dir = argv
    copy_files_and_remove_package(src_dir,dest_dir)

