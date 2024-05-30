import React from 'react';
import './LoadingModal.css'; // Import modal styles here

function LoadingModal(props) {
    const closeLoadingModal = (e) => {
        let target = e.target;
        if (target.classList.contains('modal-wrapper')){
            props.setIsLoadingModalOpen(false);
        }
    };
    return (
        <div className='loading-modal-wrapper' onClick={closeLoadingModal}>
            <div className="loading-modal-content">
                <p>{props.loadingMessage}</p>
                <div className="loading-spinner"></div>
            </div>
        </div>
    );
}

export default LoadingModal;