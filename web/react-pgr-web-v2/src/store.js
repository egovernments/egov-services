import { createStore, compose, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import { persistStore, autoRehydrate } from 'redux-persist';
import reducers from './reducer';

const middlewares = [];

middlewares.push(thunk);

if (process.env.NODE_ENV === 'development') {
  const { logger } = require('redux-logger');
  middlewares.push(logger);
}

const store = createStore(reducers, {}, compose(applyMiddleware(...middlewares), autoRehydrate()));

export default store;
