import sys
import subprocess
import os
import shutil
from tqdm import tqdm


def save_to_csv(columns, *arrays):
    """
    Saving data to csv. Each argument after columns is considered data to put inside the csv.
    :param columns: header of the csv
    :param arrays: each array of data to unpack
    :return:
    """
    header = ','.join(columns)
    header += '\n'
    lines = [header]

    # append columns name
    for data in zip(*arrays):
        row = ','.join([str(d) for d in data])
        row += '\n'
        lines.append(row)

    with open('output.csv', 'w') as csvfile:
        print('saving results...')
        csvfile.writelines(lines)


def count_class(id_commit, file_ext):
    """
    Count files with a specific extension from the id_commit
    :param id_commit:
    :param file_ext:
    :return:
    """
    nc = 0
    reset_git(id_commit)
    #count
    out = subprocess.run(['find', '.', '-type', 'f', '-name', f'*.{file_ext}'], stdout=subprocess.PIPE)
    out_path = out.stdout.decode('utf-8')
    nc = int(len(out_path.split('\n'))) # -1 ????
    return nc


def reset_git(id_commit):
    """
    Set the head of the repo to a specific commit id
    :param id_commit: Where to reset the head
    :return:
    """
    out = subprocess.run(['git', 'reset', '--hard', id_commit], stdout=subprocess.PIPE)


def read_ids(name_main_branch='main'):
    list_ids = []
    results = subprocess.run(['git', 'rev-list', name_main_branch], stdout=subprocess.PIPE).stdout.decode('utf-8')
    list_ids = results.split('\n')
    return list_ids[:-1]


def clone_repo(url):
    """
    créer un folder avec le clone du repository reçu en paramètre
    :param url: Url du repository
    :return: True si la commande git a fonctionné, False sinon
    """
    clone = subprocess.run(['git', 'clone', url, './cloned_repo'], stdout=subprocess.PIPE)
    if os.path.isdir('./cloned_repo'):
        print(clone.stdout.decode('utf-8'))
        return True
    else:
        print(clone.stdout.decode('utf-8'))
        return False


def clean_repo():
    """
    Delete the folder where the repo to analyse was cloned
    :return:
    """
    if os.path.isdir('./cloned_repo'):
        print('removing ./cloned_repo and all subdirectories')
        shutil.rmtree('./cloned_repo')  # delete all directory if it already exist


def fetch_and_save_data(url):
    print(f'calling, {url}')  # Press Ctrl+F8 to toggle the breakpoint.
    if not clone_repo(url):
        raise ValueError(f"Could not clone the repository of {url}")
    os.chdir('./cloned_repo/')
    list_ids = read_ids('main')
    NC = []
    for id_commit in tqdm(list_ids):
        nc_current = count_class(id_commit, 'py')
        NC.append(nc_current)
    os.chdir('..')
    save_to_csv(['commit_id', 'NC'], list_ids, NC)
    clean_repo()

def main(url):
    """Entry point of proto"""
    print('fetching data')
    fetch_and_save_data(url) # tache 1

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    args = sys.argv
    if (len(args) == 1):
        raise ValueError("Missing arguments...")
    if (len(args) > 2):
        raise ValueError("To many arguments...")

    main(args[1])

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
