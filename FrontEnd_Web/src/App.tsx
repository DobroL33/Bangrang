import React from 'react';
import { Route, Routes, useLocation } from "react-router-dom";

import './App.css';

import Navbar from './components/navbar';

import IntroHome from './pages/introhome';
import ManageHome from './pages/managehome';
import Login from './pages/login';
import NotFound from './pages/notfound';
import SignUp from './pages/signup';
import AdminHome from './pages/adminhome';

function App() {
  const location = useLocation();
  return (
    <div className="App">
      <Navbar />      
      <Routes location={location}>
        <Route path="/" element={<IntroHome />} />
        <Route path="/admin" element={<AdminHome />} />
        <Route path="/manage" element={<ManageHome />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        {/* 없는 페이지 출력 */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </div>
  );
}

export default App;
