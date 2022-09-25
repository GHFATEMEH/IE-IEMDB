import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "./main/front/pages/Home/Home";

function App() {
  return (
    <BrowserRouter>
          <Routes>
            <Route path="/" element={<Home />}>

            </Route>
          </Routes>
        </BrowserRouter>
  );
}

export default App;
