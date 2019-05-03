const { Client } = require("pg");

export default callback => {
  // connect to a database if needed, then pass it to `callback`:
  const pg = new Client({
    connectionString: process.env.DATABASE_URL,
    ssl: true
  });

  pg.connect();
  callback({ pg });
};
