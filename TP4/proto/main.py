import sys
import subprocess
import os
import shutil
from tqdm import tqdm  # dependencies
import argparse
import pandas as pd

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


def count_class(file_ext):
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
    os.chdir(PATH_TO_REPO)
    results = subprocess.run(['git', 'rev-list', name_main_branch], stdout=subprocess.PIPE).stdout.decode('utf-8')
    list_ids = results.split('\n')
    os.chdir('..')
    return list_ids[:-1]


def clone_repo(url):
    """
    créer un folder avec le clone du repository reçu en paramètre
    :param url: Url du repository
    :return: True si la commande git a fonctionné, False sinon
    """
    clone = subprocess.run(['git', 'clone', url, PATH_TO_REPO], stdout=subprocess.PIPE)
    if os.path.isdir(PATH_TO_REPO):
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
        # print(f'removing {PATH_TO_REPO} and all subdirectories')
        shutil.rmtree(PATH_TO_REPO)  # delete all directory if it already exist


def fetch_and_save(url='', branch_name='', file_ext='', out_csv='.', start=0, end=None):
    print(f'calling, {url}')
    if not clone_repo(url):
        raise ValueError(f"Could not clone the repository of {url}")
    list_ids = read_ids(branch_name)
    nc_list = []
    mWMC_list = []
    mBC_list = []
    for id_commit in tqdm(list_ids[start:end]):
        reset_git(id_commit)
        nc_current = count_class(file_ext)
        # CALL METRIC ANALYSER
        mWMC, mBC = metric_analyser('TP1.jar', PATH_TO_REPO, f'.{file_ext}', out_csv)
        nc_list.append(nc_current)
        mWMC_list.append(mWMC)
        mBC_list.append(mBC)
    save_to_csv(['commit_id', 'nc_list', 'mWMC', 'mBC'], list_ids, nc_list, mWMC_list, mBC_list) # INUTILE
    clean_repo()


def metric_analyser(path_to_tp1, path_to_analyse, file_ext, output_path):
    # java -jar TP1.jar path/to/project/to/analyse .java output/path/for/csv/
    mean_WMC, mean_BC = 0, 0
    stdout = subprocess.run(["java", "-jar", path_to_tp1, path_to_analyse, file_ext, output_path], stdout=subprocess.PIPE)
    df = pd.read_csv(f'{output_path}/classes.csv')
    clean_metric_analyse_output(output_path)
    return df.WMC.mean(), df.classe_BC.mean()


def clean_metric_analyse_output(output_path):
    if os.path.isdir(output_path):
        # print(f'\nremoving {output_path} and all subdirectories')
        shutil.rmtree(output_path)  # delete all directory if it already exist


def main(url, branch_name, file_ext,out_csv, start, end):
    """Entry point of proto"""
    print('fetching data')
    fetch_and_save(url, branch_name, file_ext, out_csv, start, end)  # tache 1


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--url',
                        help='url of the repository to analyse',
                        type=str,
                        default='https://github.com/jfree/jfreechart')
    parser.add_argument('--branch-name',
                        help='Name of the main branch, usually main or master',
                        type=str,
                        default='master')
    parser.add_argument('--type',
                        help='File extension to look for in the analysis',
                        type=str,
                        default='java')
    parser.add_argument('--out',
                        help='url of the repository to analyse',
                        type=str,
                        default='./output_analyse/')
    parser.add_argument('--start',
                        help='Analyse commits from [start:end]',
                        type=int,
                        default=0)
    parser.add_argument('--end',
                        help='Analyse commits from [start:end]',
                        type=int,
                        default=0)
    args = parser.parse_args()

    if args.end == 0:
        args.end = None

    main(url=args.url,
         branch_name=args.branch_name,
         file_ext=args.type,
         out_csv=args.out,
         start=args.start,
         end=args.end)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
