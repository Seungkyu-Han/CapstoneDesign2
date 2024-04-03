import './App.css';
import { useState, useEffect } from 'react';
import { Routes, Route, useLocation, useNavigate } from 'react-router-dom';
import DiaryList from './routes/DiaryList';
import NavigationMenu from './components/NavigationMenu';


function App() {
  const [activePage, setActivePage] = useState('');
  const location = useLocation();
  const navigate = useNavigate();
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
            <NavigationMenu activePage={activePage} navigate={navigate} toggleDropdown={toggleDropdown} />
          </div>
        </div>
        <ul className="navbar">
          <NavigationMenu activePage={activePage} navigate={navigate} toggleDropdown={toggleDropdown} />
        </ul>
      </nav>
      <Routes>
        <Route path='/' element={<DiaryList />}/>
      </Routes>
    </div>
  );
}

export default App;
