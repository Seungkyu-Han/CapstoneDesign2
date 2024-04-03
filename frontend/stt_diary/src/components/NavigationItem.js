
function NavigationItem({ path, activePage, onClick, title }) {
    return (
      <li>
        <button className={activePage === path ? 'active' : ''} onClick={onClick}>
          {title}
        </button>
      </li>
    );
}

export default NavigationItem;