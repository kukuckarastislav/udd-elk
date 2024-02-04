
import style from './IndexContractPage.module.scss'
import { useState } from 'react';
import axios from 'axios';
import { Alert, TextField, Typography } from '@mui/material';

import CheckIcon from '@mui/icons-material/Check';
import { ContractParsedDataDTO } from '../models/ContractParsedDataDTO';

export default function IndexContractPage() {

  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadedState, setUploadedState] = useState(0);
  const [contractParsedDataDTO, setContractParsedDataDTO] = 
  useState(new ContractParsedDataDTO());

  const handleFileChange = (event: any) => {
    setContractParsedDataDTO(new ContractParsedDataDTO());
    if(event.target.files[0]){
      setUploadedState(1);
    }
    else{
      setUploadedState(0);
    }
    setSelectedFile(event.target.files[0]);
  };

  const handleParse = () => {
    if (!selectedFile) {
      alert('Please select a file to upload');
      return;
    }

    const formData = new FormData();
    formData.append('documentFile', selectedFile);
    setUploadedState(2);
    axios.post('http://localhost:8080/api/index/contract/parse', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(response => {
      console.log('File parsed successfully', response.data);
      setContractParsedDataDTO(response.data);
      setUploadedState(3);
    })
    .catch(error => {
      console.error('Error uploading file:', error);
    });
  };

  const handleUpload = () => {
    if (!selectedFile) {
      alert('Please select a file to upload');
      return;
    }
    setUploadedState(4);

    const formData = new FormData();
    formData.append('documentFile', selectedFile);
    formData.append('contractParsedDataDTO', JSON.stringify(contractParsedDataDTO));
    

    axios.post('http://localhost:8080/api/index/contract', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    .then(response => {
      console.log('File uploaded successfully. Server filename:', response.data);
      setUploadedState(5);
    })
    .catch(error => {
      console.error('Error uploading file:', error);
    });
  };

  return (
    <div className={style.IndexContractPageCss}>
      <div className='flex flex-col justify-center gap-4 rounded-md shadow-sm p-6 bg-slate-50'>
        <Typography variant='h5'>Upload new Contract</Typography>
        <input className='fileInputCss' type="file" onChange={handleFileChange} accept=".pdf" />
        <button hidden={!(uploadedState==1 || uploadedState==2)} className="cool-btn w-fit" 
          onClick={handleParse}>
          {uploadedState == 1 ? 'Parse' : 'Parsing ...'}
          </button>
        {uploadedState >= 3 && 
          <Alert icon={<CheckIcon fontSize="inherit" />} severity="success">
            File parsed successfully
          </Alert>
        }


        <div className='flex flex-col gap-4 mt-8'>
          <div className='block'>
            <span className="block text-sm font-medium text-slate-700">Potpisanik ugovora</span>
            <input type="text" value={contractParsedDataDTO.employeeFullName} className="cool-input"
              onChange={(event) => {
                setContractParsedDataDTO(new ContractParsedDataDTO(event.target.value,
                  contractParsedDataDTO.governmentName, contractParsedDataDTO.governmentLevel, contractParsedDataDTO.governmentAddress));
              }}
            />
          </div>

          <div className='block'>
            <span className="block text-sm font-medium text-slate-700">Naziv Vlade</span>
            <input type="text" value={contractParsedDataDTO.governmentName} className="cool-input"
              onChange={(event) => {
                setContractParsedDataDTO(new ContractParsedDataDTO(contractParsedDataDTO.employeeFullName,
                  event.target.value, contractParsedDataDTO.governmentLevel, contractParsedDataDTO.governmentAddress));
              }}
            />
          </div>

          <div className='block'>
            <span className="block text-sm font-medium text-slate-700">Adresa vlade</span>
            <input type="text" value={contractParsedDataDTO.governmentAddress} className="cool-input"
              onChange={(event) => {
                setContractParsedDataDTO(new ContractParsedDataDTO(contractParsedDataDTO.employeeFullName,
                  contractParsedDataDTO.governmentName, contractParsedDataDTO.governmentLevel, event.target.value));
              }}
            />
          </div>

          <div className='block'>
            <span className="block text-sm font-medium text-slate-700">Nivo vlade</span>
            <select value={contractParsedDataDTO.governmentLevel} className="cool-input"
              onChange={(event) => {
                setContractParsedDataDTO(new ContractParsedDataDTO(contractParsedDataDTO.employeeFullName,
                  contractParsedDataDTO.governmentName, event.target.value, contractParsedDataDTO.governmentAddress));
              }}>
              <option value="OPSTINSKA">OPSTINSKA</option>
              <option value="GRADSKA">GRADSKA</option>
              <option value="POKRAJINSKA">POKRAJINSKA</option>
              <option value="DRZAVNA">DRZAVNA</option>
            </select>
          </div>

        </div>

        <button hidden={!(uploadedState == 3 || uploadedState == 4)} className="cool-btn w-fit" 
          onClick={handleUpload}>
            {uploadedState == 3 ? 'Uploud' : 'Uplouding ...'}
        </button>

        {uploadedState == 5 && 
          <Alert icon={<CheckIcon fontSize="inherit" />} severity="success">
            File uploaded successfully
          </Alert>
        }
        
        
      </div>
    </div>
  );
}