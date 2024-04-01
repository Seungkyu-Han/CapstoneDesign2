import './App.css';
import { useState, useEffect } from 'react';
import { Routes, Route, Link, useLocation } from 'react-router-dom';
import DiaryList from './routes/DiaryList';

function App() {
  const [activePage, setActivePage] = useState('');
  const location = useLocation();
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };
  
  useEffect(() => {
    const path = location.pathname;
    const page = path.split("/").pop();
    setActivePage(page);
  }, [location]);

  return (
    <div className="App">
      <nav>
        <div className='mobileNavBar'>
          <img src={require('./assets/mobileLogo.png')} alt="" className='mobileLogo' />
          <button className="dropdown-btn" onClick={() => toggleDropdown()}>
            <img src={require('./assets/barIcon.png')} alt=""></img>
          </button>
          <div className={`dropdown-content ${isDropdownOpen ? 'show' : ''}`}>
            <li><Link to="/" className={activePage === '' ? 'active' : ''} onClick={() => toggleDropdown()}>나의 일기장</Link></li>
            <li><Link to="/analysis" className={activePage === 'analysis' ? 'active' : ''} onClick={() => toggleDropdown()}>나의 감정분석</Link></li>
            <li><Link to="/login" className={activePage === 'login' ? 'active' : ''} onClick={() => toggleDropdown()}>로그아웃</Link></li>
          </div>
        </div>
        <ul className="navbar">
          <li><Link to="/" className={activePage === '' ? 'active' : ''}>나의 일기장</Link></li>
          <li><Link to="/analysis" className={activePage === 'analysis' ? 'active' : ''}>나의 감정분석</Link></li>
          <li><Link to="/login" className={activePage === 'login' ? 'active' : ''}>로그아웃</Link></li>
        </ul>
      </nav>
      <Routes>
        <Route path='/' element={<DiaryList />}/>
      </Routes>
    </div>
  );
}

export default App;
