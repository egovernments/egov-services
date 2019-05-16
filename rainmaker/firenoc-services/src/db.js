const { Pool } = require("pg");
import {DB_HOST,DB_NAME,DB_PORT,DB_USER,DB_SSL,DB_PASSWORD, DB_MAX_POOL_SIZE} from './envVariables'

// Use connection pool to limit max active DB connections

const pool = new Pool({
  user: DB_USER,
  host: DB_HOST,
  database: DB_NAME,
  password: DB_PASSWORD,
  ssl: DB_SSL,
  port: DB_PORT,
  max: DB_MAX_POOL_SIZE,
  idleTimeoutMillis: 30000,
  connectionTimeoutMillis: 2000,
})

// Expose method, log query, initiate trace etc at single point later on.
module.exports = {
  query: (text, params) => pool.query(text, params)
}
