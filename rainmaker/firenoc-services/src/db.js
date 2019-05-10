const { Client } = require("pg");

export default callback => {
  // connect to a database if needed, then pass it to `callback`:
  const {DB_HOST,DB_NAME,DB_PORT,DB_USER,DB_PASSWORD}=process.env;
  const pg = new Client({
    user: process.env.DB_USER,
    host: process.env.DB_HOST,
    database: process.env.DB_NAME,
    password: process.env.DB_PASSWORD,
    ssl: process.env.DB_SSL,
    port: process.env.DB_PORT,
  });

  pg.connect();

  callback(pg);
};
