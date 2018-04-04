[![Build Status](https://travis-ci.org/Chocorean/ClientMinecraftTNCY.svg?branch=master)](https://travis-ci.org/Chocorean/ClientMinecraftTNCY)
[![vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=vulnerabilities)](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=vulnerabilities)
[![vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=alert_status
)](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=alert_status
)
[![code smells](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=code_smells
)](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=code_smells
)
[![bugs](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=bugs
)](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=bugs
)
[![duplicate](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=duplicated_lines_density
)](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean%3Aminecraft-updater&metric=duplicated_lines_density
)








# ClientMinecraftTNCY

## Sommaire

* [Introduction](#introduction)
* [License](#license)
* [Utilisation](#utilisation)
* [Téléchargement](#téléchargement)
* [Problèmes](#problèmes)
* [Auteur](auteur)

## Introduction

Cette application a pour but d'aider les élèves de Télécom NANCY à mettre à jour leur client Minecraft suite aux mises à jour du serveur moddé de Télécom.
Elle contient le *changelog*, et permet de mettre à jour automatiquement son client pour pouvoir jouer sans problème.

## License

Voir [LICENSE](https://github.com/Chocorean/ClientMinecraftTNCY/blob/master/LICENSE).

## Création du jar

```bash
mvn clean package
```

## Utilisation

Exécuter l'application avec `java -jar Updater.jar`. Renseigner le chemin de destination où enregistrer les mods (le contenu du dossier sera nettoyé avant téléchargement), et mettre à jour.

## Téléchargement

Rendez-vous [ici](https://gitlab.com/telecomnancy.net/public/wikis/Serveur-minecraft) pour le téléchargement et quelques informations supplémentaires.

## Problèmes

Si vous rencontrez des problèmes quant à l'utilisation de cet *updater*, merci de bien vouloir créer une *Issue* dans la section dédiée.

## Auteur

Baptiste Chocot ([@Chocorean](https://github.com/Chocorean/))
