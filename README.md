# Coachable

The original source code for the Coachable web app at https://www.coachablefitness.com before the migration to Supabase.

## Technologies Used

- Node.js: `18.12.1`
- Java: `17`
- PostgreSQL
- pgAdmin
- IntelliJ IDEA/community edition -OR- vscode with java/spring boot extensions
- Webstorm -OR- vscode with Sveltekit extensions
- Extension for Elixir in your IDE of choice.

## Set Up Instructions

1. Install Node.js `18.12.1`
2. Install Java `17`
3. Install PostgreSQL using the installer provided by PostgreSQL. This will install pgAdmin, Postgres, and psql.
4. Install either IntelliJ IDEA or community edition, or vscode with java/spring boot extensions.
5. Install either Webstorm or vscode with Sveltekit extensions.
6. Install an extension for Elixir in whichever IDE you are using.
7. Create a `.env` file for the Java project at the root of the project.
8. Create a `.env` file in the client/ folder.
9. Create a `.env` file for the socket/ folder.
10. Create a new database in pgadmin, and use the user/password/database name in your java .env file.

## Usage

1. Run the Java project.
2. Run the Sveltekit project.
3. Open a web browser and go to `http://localhost:5173`.

## Contributing/Features

The typical flow for contributing will look like this: 

First, if you are initiating a new feature - check out the `develop` branch and ensure you have fetched all the latest 
changes. If you are starting a new feature branch, run

```git checkout -b feature-<FEATURE-NAME>```

If there is an active feature branch you are contributing to, checkout that feature branch and branch off of it:

```git checkout -b <YOUR-NAME>-<TICKET/FEATURE-NAME>```

When you are ready, make a PR to be merged into the `feature` branch. After being approved, when the feature is complete,
a PR will be made to the `develop` branch. Once the new feature or features are finished and ready to be deployed, a 
final PR will be made for the `main` branch, where the code will automatically be deployed.

## Migrations

The goal is to only add 1 migration per feature. However, this can be broken if two people are working on separate feature
branches that will be merged into the same feature branch at a later time.

In order to keep to one migration in most cases, create a migration file in src/main/resources/db.migrations. Add the migration
code and restart your application to apply the migration. Later, if you need to make changes to your migration, go to 
the query tool in pgAdmin and delete the most recent migration from the flyway_schema_history table, and then make any changes
necessary to your migration file. When you are ready, start the application again to apply the new changes.

## License

