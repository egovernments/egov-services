import { createStore, compose, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import { persistStore, autoRehydrate } from 'redux-persist';
import reducers from './reducer';

const store = createStore(
  reducers,
  {},
  compose(
    applyMiddleware(thunk),
    autoRehydrate()
  )
);

persistStore(store);

export default store;
