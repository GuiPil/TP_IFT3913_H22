import sys
import subprocess
import os
import shutil
from tqdm import tqdm  # dependencies

PATH_TO_REPO = './cloned_repo'


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
    # count
    os.chdir(PATH_TO_REPO)
    out = subprocess.run(['find', '.', '-type', 'f', '-name', f'*.{file_ext}'], stdout=subprocess.PIPE)
    out_path = out.stdout.decode('utf-8')
    nc = int(len(out_path.split('\n')))  # -1 ????
    os.chdir('..')
    return nc


def reset_git(id_commit):
    """
    Set the head of the repo to a specific commit id
    :param id_commit: Where to reset the head
    :return:
    """
    os.chdir(PATH_TO_REPO)
    out = subprocess.run(['git', 'reset', '--hard', id_commit], stdout=subprocess.PIPE)
    os.chdir('..')


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
    if os.path.isdir(PATH_TO_REPO):
        print(f'removing {PATH_TO_REPO} and all subdirectories')
        shutil.rmtree(PATH_TO_REPO)  # delete all directory if it already exist


def fetch_and_save_id_nc(url):
    print(f'calling, {url}')  # Press Ctrl+F8 to toggle the breakpoint.
    if not clone_repo(url):
        raise ValueError(f"Could not clone the repository of {url}")
    list_ids = read_ids('main')
    nc_list = []
    mWMC_list = []
    mBC_list = []
    for id_commit in tqdm(list_ids):
        reset_git(id_commit)
        nc_current = count_class(id_commit, 'py')
        nc_list.append(nc_current)
        # CALL METRIC ANALYSER
        # mWMC, mBC = metric_analyser('TP1.jar', PATH_TO_REPO, '.java', './output_analyser.csv')
        # mWMC_list.append(mWMC)
        # mBC_list.append(mWMC)
    save_to_csv(['commit_id', 'nc_list'], list_ids, nc_list) # INUTILE
    clean_repo()

def metric_analyser(path_to_tp1, path_to_analyse, file_ext, output_path):
    # java -jar TP1.jar path/to/project/to/analyse .java output/path/for/csv/

    mean_WMC, mean_BC = 0, 0

    subprocess.run(["java", "-jar", path_to_tp1, path_to_analyse, file_ext, output_path])
    fetch_mWMC(output_path)
    fetch_mBC(output_path)
    clean_metric_analyse_output(output_path)
    return mean_WMC, mean_BC

def fetch_mWMC(output_path):
    pass


def fetch_mBC(output_path):
    pass

def clean_metric_analyse_output():
    pass

def main(url):
    """Entry point of proto"""
    print('fetching data')
    fetch_and_save_id_nc(url)  # tache 1


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    args = sys.argv
    if (len(args) == 1):
        raise ValueError("Missing arguments...")
    if (len(args) > 2):
        raise ValueError("To many arguments...")

    main(args[1])

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
