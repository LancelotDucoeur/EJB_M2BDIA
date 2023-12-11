# README pour lancer un projet EJB3 avec WildFly

## Introduction
Ce document fournit les instructions pour lancer un projet EJB3 en utilisant le serveur d'applications WildFly.

## Étapes pour lancer le projet

1. **Démarrer le serveur WildFly** :
   Ouvrez un terminal et naviguez vers le dossier d'installation de WildFly. Utilisez la commande suivante pour démarrer le serveur avec une configuration spécifique :

    ```bash
    cd banque
   ```

   ```bash
   ./bin/standalone.sh --server-config=standalone-full-ha.xml
   ```

## Accès à l'Application

Une fois l'application lancée, vous pouvez y accéder via le lien suivant :
http://127.0.0.1:8080/bank-web


# identifiant Wildfly
user/password: wildfly/wildfly