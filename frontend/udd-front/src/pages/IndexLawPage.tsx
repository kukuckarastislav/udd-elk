import { useState } from 'react';
import style from './IndexLawPage.module.scss'
import axios from 'axios';
import { Button } from '@mui/material';

export default function IndexLawPage() {
  // i want to uploud file pdf to server

  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (event: any) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = () => {
    if (!selectedFile) {
      alert('Please select a file to upload');
      return;
    }

    const formData = new FormData();
    formData.append('documentFile', selectedFile);

    axios.post('http://localhost:8080/api/index/law', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(response => {
      console.log('File uploaded successfully. Server filename:', response.data);
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
        <input type="file" onChange={handleFileChange} accept=".pdf" />
        <Button variant='contained' className='w-fit' onClick={handleUpload}>Upload</Button>
      </div>
    </div>
  );
}
