const { Client } = require("pg");


export default callback => {
  // connect to a database if needed, then pass it to `callback`:
  const {DB_HOST,DB_NAME,DB_PORT,DB_USER,DB_SSL,DB_PASSWORD}=process.env;
  const pg = new Client({
    user: DB_USER,
    host: DB_HOST,
    database: DB_NAME,
    password: DB_PASSWORD,
    ssl: DB_SSL,
    port: DB_PORT,
  });

  pg.connect();

  callback(pg);
};
