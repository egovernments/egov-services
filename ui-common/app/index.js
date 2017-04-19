import React from 'react';
import { render } from 'react-dom';
import Button from './button';
import '../node_modules/bootstrap/dist/css/bootstrap.css';

render(
  <div>
    <h1>Hello World</h1>
    <Button text='Hello'/>
  </div>,
  document.getElementById('root')
);
