import React from 'react';
import './RecordModal.css'; // Import modal styles here
import { offRecAudio, onSubmitAudioFile } from '../utils/record';

function RecordModal(props) {

    const closeModal = (e) => {
        props.setIsRecordModalOpen(false);
    };

    const callSTT = (data) => {
        fetch(process.env.REACT_APP_AI_API_URL + '/whisper', {
            method: 'POST',
            body: data
        })
        .then(res => console.log(res))
        .catch(err => {
            console.log('서버 오류입니다.');
        })
    }

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
                <button className='save-record-button' onClick={() => {
                    offRecAudio(props.setAudioUrl, props.setOnRec, props.analyser, props.source, props.media, props.stream);
                    let result = onSubmitAudioFile();
                    callSTT(result);
                    closeModal(props.audioUrl);
                }}>
                    <img src={require('../assets/save-record-icon.png')} alt="" />
                </button>
            </div>
        </div>
    );
}

export default RecordModal;
