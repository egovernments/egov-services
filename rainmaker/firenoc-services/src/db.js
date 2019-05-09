const { Client } = require("pg");

const {DB_HOST,DB_NAME,DB_PORT,DB_USER,DB_PASSWORD}=process.env;

// console.log("env",TERM);
//
// console.log(process.env);
//
// console.log("envtest2",process.env.DB_USER);


export default callback => {
  // connect to a database if needed, then pass it to `callback`:
  const pg = new Client({
    connectionString: `postgres://${DB_USER}:${DB_PASSWORD}@${DB_HOST}:${DB_PORT}/${DB_NAME}`,
    ssl: true
  });

  pg.connect();

  callback(pg);
};
