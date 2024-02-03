import { useState } from 'react';
import style from './IndexLawPage.module.scss'
import axios from 'axios';
import { Alert, Typography } from '@mui/material';

import CheckIcon from '@mui/icons-material/Check';

export default function IndexLawPage() {
  // i want to uploud file pdf to server

  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadedState, setUploadedState] = useState(0);

  const handleFileChange = (event: any) => {
    setUploadedState(1);
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = () => {
    if (!selectedFile) {
      alert('Please select a file to upload');
      return;
    }

    const formData = new FormData();
    formData.append('documentFile', selectedFile);
    setUploadedState(2);
    axios.post('http://localhost:8080/api/index/law', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(response => {
      console.log('File uploaded successfully. Server filename:', response.data);
      //alert('File uploaded successfully');
      setUploadedState(3);
      // You can handle the response as needed.
    })
    .catch(error => {
      console.error('Error uploading file:', error);
      // Handle the error as needed.
    });
  };

  return (
    <div className={style.IndexLawPageCss}>
      <div className='flex flex-col justify-center gap-4 rounded-md shadow-sm p-4 bg-slate-50'>
        <Typography variant='h5'>Upload a PDF file to index law</Typography>
        <input className='fileInputCss' type="file" onChange={handleFileChange} accept=".pdf" />
        <button hidden={uploadedState==0 || uploadedState==3} className="cool-btn w-fit" 
        onClick={handleUpload}>{uploadedState == 1 ? 'Uploud' : 'Uplouding ...'}</button>
        {uploadedState == 3 && 
          <Alert icon={<CheckIcon fontSize="inherit" />} severity="success">
            File uploaded successfully
          </Alert>
        }
      </div>
    </div>
  );
}
