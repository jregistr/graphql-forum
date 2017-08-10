# graphql-forum
Project made to experiment with GraphQL and Sangria library.

## Usage
You will need to have ready a PostgreSQL database and a user.

* Copy the contents of application.ex.conf under the `conf` folder
into another file _application.conf_ under the same directory.

* Configure the data store by updating the lines displayed
below with database credentials.

```
slick.dbs.default.db.url="jdbc:postgresql://localhost/<DATABASE NAME>"
slick.dbs.default.db.user="<DATABASE USER NAME>"
slick.dbs.default.db.password="<DATABASE PASSWORD>"
```

Issuing `sbt run` should have it up and running on
`localhost:9000` where a GraphIQL interface will be presented.
