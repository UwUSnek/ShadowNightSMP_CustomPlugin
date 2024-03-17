import os
import pathlib



# Finds all files in a directory, recursively.
# Returns a list of dictionaries each containing the path of the file relative to the current working directory and the path of the file relative to the <start> directory
def list_files_recursive(start, search_path=""):
    r = []
    if len(search_path) == 0:
        search_path = start

    for entry in os.listdir(search_path):
        entry_path = os.path.join(search_path, entry)
        if os.path.isdir(entry_path):
            r += list_files_recursive(start, entry_path)
        else:
            r += [{ "path": entry_path, "rel_path": os.path.relpath(entry_path, start) }]

    return r




# Creates all parent directories of <path> and <path> itself if it is a directory (has no extension).
# Does nothing if the directories already exist.
# Returns <path>
def mkdirs(path: str):
    os.makedirs(path if len(path.split("/")[-1].split(".")) <= 1 else pathlib.Path(path).parent, exist_ok=True)
    return path