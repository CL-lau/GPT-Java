import React from "react";
import { BrowserRouter as Router, Route, Switch, Redirect } from "react-router-dom";
import HomePage from "./pages/HomePage";
import ChatGPTChat from "./components/ChatGPTChat.js"
// import OtherPage from "./pages/OtherPage"; // 你可能有其他页面

const App = () => {
    return (
        <Router>
            <Switch>
                {/* 聊天页面 */}
                <Route exact path="/" component={ChatGPTChat} />

                {/* 其他页面 */}
                <Route path="/HomePage" component={HomePage} />

                {/* 如果用户输入了不存在的路径，则重定向到聊天页面 */}
                <Redirect to="/" />
            </Switch>
        </Router>
    );
};

export default App;
