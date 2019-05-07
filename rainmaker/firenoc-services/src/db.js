const { Client } = require("pg");

export default callback => {
  // connect to a database if needed, then pass it to `callback`:
  const pg = new Client({
    connectionString: 'postgres://thpqnqhvqfbvqw:46ad4cd2bd2d8d2f3a6cb567482b6c703473dc0eaf150baea5a92909131302b5@ec2-54-225-68-133.compute-1.amazonaws.com:5432/d9jf2v9doprlot',
    // process.env.DATABASE_URL,
    ssl: true
  });

  pg.connect();

  callback(pg);
};
