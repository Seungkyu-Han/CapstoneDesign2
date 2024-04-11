import './App.css';
import { useState } from 'react';
import { Routes, Route} from 'react-router-dom';
import DiaryList from './routes/DiaryList';
import SentimentReport from './routes/SentimentReport';
import NavigationMenu from './components/NavigationMenu';

function App() {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };
  

  return (
    <div className="App">
      <nav>
        <div className='mobileNavBar'>
          <img src={require('./assets/mobileLogo.png')} alt="" className='mobileLogo' />
          <button className="dropdown-btn" onClick={() => toggleDropdown()}>
            <img src={require('./assets/barIcon.png')} alt=""></img>
          </button>
          <div className={`dropdown-content ${isDropdownOpen ? 'show' : ''}`}>
            <NavigationMenu toggleDropdown={toggleDropdown} />
          </div>
        </div>
        <ul className="navbar">
          <NavigationMenu toggleDropdown={toggleDropdown} />
        </ul>
      </nav>
      <Routes>
        <Route path='/' element={<DiaryList />}/>
        <Route path='/analysis' element={<SentimentReport/>}/>
      </Routes>
    </div>
  );
}

export default App;
