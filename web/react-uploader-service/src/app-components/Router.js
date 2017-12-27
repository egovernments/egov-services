import React from "react";
import { Route, Switch } from "react-router-dom";
import FileUploader from "../containers/FileUploader";
import UserJobs from "../containers/UserJobs";

const Main = () => {
  return (
    <main>
      <Switch>
        <Route exact path="/" component={FileUploader} />
        <Route exact path="/user-jobs" component={UserJobs} />
      </Switch>
    </main>
  );
};

export default Main;
