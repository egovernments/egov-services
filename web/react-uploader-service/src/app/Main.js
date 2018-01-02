import React from "react";
import { Route, Switch } from "react-router-dom";
import FileUploader from "../create-job";
import UserJobs from "../jobs";

const Main = () => {
  return (
    <main>
      <Switch>
        <Route exact path="/user-jobs" component={UserJobs} />
        <Route exact path="/" component={FileUploader} />
      </Switch>
    </main>
  );
};

export default Main;
