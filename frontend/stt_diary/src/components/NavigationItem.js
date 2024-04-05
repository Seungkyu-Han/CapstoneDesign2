import React from 'react';
import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function NavigationItem({ path, navigate, toggleDropdown, title}) {
  const [activePage, setActivePage] = useState('');
  const location = useLocation();

  useEffect(() => {
    const path = location.pathname;
    const page = path.split("/").pop();
    setActivePage(page);
  }, [location]);
  
  return (
    <li>
      <button className={activePage === path ? 'active' : ''} onClick={() => {navigate('/' + path); toggleDropdown();}}>
        {title}
      </button>
    </li>
  );
}

export default NavigationItem;