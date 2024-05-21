import React, { useEffect } from 'react';
import './RecordModal.css'; // Import modal styles here

function RecordModal(props) {

    const closeModal = (e) => {
        props.setIsRecordModalOpen(false);
        props.setIsLoadingModalOpen(true);
    };


    return (
        <div className='modal-wrapper'>
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
                <button className='save-record-button' onClick={async () => {
                    props.offRecAudio();
                    closeModal();
                }}>
                    <img src={require('../assets/save-record-icon.png')} alt="" />
                </button>
            </div>
        </div>
    );
}

export default RecordModal;
