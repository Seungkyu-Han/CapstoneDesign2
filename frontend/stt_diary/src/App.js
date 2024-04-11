import './App.css';
import { useState, useEffect } from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import DiaryList from './routes/DiaryList';
import SentimentReport from './routes/SentimentReport';

function App() {
  return (
    <div className="App">
      <nav>
        {/* 여기에 navbar 만들면 됩니다. */}
      </nav>
      <Routes>
        <Route path='/' element={<DiaryList />}/>
        <Route path='/analysis' element={<SentimentReport/>}/>
      </Routes>
    </div>
  );
}

export default App;
