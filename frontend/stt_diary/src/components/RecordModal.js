import React from 'react';
import './RecordModal.css'; // Import modal styles here

function RecordModal({setIsRecordModalOpen }) {
    const closeModal = (e) => {
        let target = e.target;
        if (target.classList.contains('modal-wrapper')){
            setIsRecordModalOpen(false);
        }
    };
    return (
        <div className='modal-wrapper' onClick={closeModal}>
            <div className="modal-content">
                <p className='p1'>말씀 해주세요 ...</p>
                <div className="record-loading-icon">
                    <span className='stroke'></span>
                    <span className='stroke'></span>
                    <span className='stroke'></span>
                    <span className='stroke'></span>
                    <span className='stroke'></span>    
                </div>
                <p className='p2'>녹음 중...</p>
                <button className='save-record-button'>
                    <img src={require('../assets/save-record-icon.png')} alt="" />
                </button>
            </div>
        </div>
    );
}

export default RecordModal;