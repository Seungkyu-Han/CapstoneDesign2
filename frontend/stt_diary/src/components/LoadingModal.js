import React from 'react';
import './LoadingModal.css'; // Import modal styles here

function LoadingModal({setIsLoadingModalOpen }) {
    const closeLoadingModal = (e) => {
        let target = e.target;
        if (target.classList.contains('modal-wrapper')){
            setIsLoadingModalOpen(false);
        }
    };
    return (
        <div className='loading-modal-wrapper' onClick={closeLoadingModal}>
            <div className="loading-modal-content">
                <p>오늘 일기로 감성 분석 중입니다 ...</p>
                <div className="loading-spinner"></div>
            </div>
        </div>
    );
}

export default LoadingModal;