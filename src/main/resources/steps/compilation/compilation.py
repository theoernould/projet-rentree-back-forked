# -*- coding: utf-8 -*-

import sys
import logging

# Nom du fichier Python en cours d'exécution sans l'extension
script_name = 'compilation'

# Configurer le logging pour écrire dans un fichier .log avec le même nom de base que le script Python
log_filename = 'src/main/resources/steps/compilation/compilation.log'
logging.basicConfig(filename=log_filename, level=logging.INFO, format='%(asctime)s:%(levelname)s:%(message)s')

print('Hello world')

# Si tout se passe bien, on écrit le nom du fichier Python et le statut dans le log
logging.info('compilation executed successfully')
