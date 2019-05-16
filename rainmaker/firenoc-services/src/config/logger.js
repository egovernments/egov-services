'use strict';

const { createLogger, format, transports } = require('winston');
import {LOG_LEVEL} from '../envVariables'

const logger = createLogger({
  level: LOG_LEVEL,
  format: 
  format.combine(
    format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss.SSSZZ' }),
    format.json()
  ),
  transports: [new transports.Console()]
});

export default logger;
