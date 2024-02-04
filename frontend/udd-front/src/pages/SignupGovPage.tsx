
import style from './SignupGovPage.module.scss'
import { useState } from 'react';
import axios from 'axios';
import { Alert, Divider, Typography } from '@mui/material';

import CheckIcon from '@mui/icons-material/Check';
import { ContractParsedDataDTO } from '../models/ContractParsedDataDTO';
import { Government } from '../models/Government';

export default function SignupGovPage() {

  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadedState, setUploadedState] = useState(0);
  const [contractParsedDataDTO, setContractParsedDataDTO] = 
  useState(new ContractParsedDataDTO());
  const [government, setGovernment] = useState(new Government());

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
    formData.append('addGovernmentDTO', JSON.stringify(government));
    formData.append('contractParsedDataDTO', JSON.stringify(contractParsedDataDTO));
    
    console.log(government);
    console.log(contractParsedDataDTO);

    

    axios.post('http://localhost:8080/api/auth/government', formData, {
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
    <div className={style.SignupGovPageCss}>
      <div className='flex flex-col justify-center gap-4 rounded-md shadow-sm p-6 bg-slate-50'>
        <Typography variant='h5'>Signup new Government and upload Contract</Typography>
        <div className='mt-4 flex justify-start gap-6'>
          <div className='flex flex-col gap-8'>

            <div className='flex flex-row gap-4'>
              <div className='flex flex-col gap-4'>
                <div className='block w-48'>
                  <span className="block text-sm font-medium text-slate-700">Government name</span>
                  <input type="text" value={government.name} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.name = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
                <div className='block w-48'>
                  <span className="block text-sm font-medium text-slate-700">Password</span>
                  <input type="password" value={government.password} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.password = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
              </div>
              <div className='flex flex-col gap-4'>
                <div className='block w-48'>
                  <span className="block text-sm font-medium text-slate-700">Government level</span>
                  <select value={government.govLevel} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.govLevel = event.target.value;
                      setGovernment(newGov);
                    }}>
                    <option value="OPSTINSKA">OPSTINSKA</option>
                    <option value="GRADSKA">GRADSKA</option>
                    <option value="POKRAJINSKA">POKRAJINSKA</option>
                    <option value="DRZAVNA">DRZAVNA</option>
                  </select>
                </div>
                <div className='block w-48'>
                  <span className="block text-sm font-medium text-slate-700">#of employees</span>
                  <input type="number" value={government.numberOfEmployees} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.numberOfEmployees = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
              </div>
            </div>

            <div>
            <Typography variant='h6'>Government address</Typography>
            <div className='flex flex-row gap-4 rounded-md p-4 border-4'>
              
              <div className='flex flex-col gap-4'>
                <div className='block'>
                  <span className="block text-sm font-medium text-slate-700">City</span>
                  <input type="text" value={government.city} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.city = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
                <div className='block'>
                  <span className="block text-sm font-medium text-slate-700">Street</span>
                  <input type="text" value={government.street} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.street = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
                <div className='block '>
                  <span className="block text-sm font-medium text-slate-700">Number</span>
                  <input type="text" value={government.number} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.number = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
              </div>
              <div className='flex flex-col gap-4'>
                <div className='block '>
                  <span className="block text-sm font-medium text-slate-700">Postal code</span>
                  <input type="text" value={government.postalCode} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.postalCode = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
                <div className='block'>
                  <span className="block text-sm font-medium text-slate-700">Country</span>
                  <input type="text" value={government.country} className="cool-input"
                    onChange={(event) => {
                      let newGov = JSON.parse(JSON.stringify(government))
                      newGov.country = event.target.value;
                      setGovernment(newGov);
                    }}
                  />
                </div>
              </div>
            </div>
            </div>


          </div>
          <Divider orientation="vertical" flexItem />
          <div className='w-full'>  
            <input className='fileInputCss  mb-4' type="file" onChange={handleFileChange} accept=".pdf" />
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
              <div className='block w-48'>
                <span className="block text-sm font-medium text-slate-700">Potpisanik ugovora</span>
                <input type="text" value={contractParsedDataDTO.employeeFullName} className="cool-input"
                  onChange={(event) => {
                    setContractParsedDataDTO(new ContractParsedDataDTO(event.target.value,
                      contractParsedDataDTO.governmentName, contractParsedDataDTO.governmentLevel, contractParsedDataDTO.governmentAddress));
                  }}
                />
              </div>

              <div className='block w-48'>
                <span className="block text-sm font-medium text-slate-700">Naziv Vlade</span>
                <input type="text" value={contractParsedDataDTO.governmentName} className="cool-input"
                  onChange={(event) => {
                    setContractParsedDataDTO(new ContractParsedDataDTO(contractParsedDataDTO.employeeFullName,
                      event.target.value, contractParsedDataDTO.governmentLevel, contractParsedDataDTO.governmentAddress));
                  }}
                />
              </div>

              <div className='block w-48'>
                <span className="block text-sm font-medium text-slate-700">Adresa vlade</span>
                <input type="text" value={contractParsedDataDTO.governmentAddress} className="cool-input"
                  onChange={(event) => {
                    setContractParsedDataDTO(new ContractParsedDataDTO(contractParsedDataDTO.employeeFullName,
                      contractParsedDataDTO.governmentName, contractParsedDataDTO.governmentLevel, event.target.value));
                  }}
                />
              </div>

              <div className='block w-48'>
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
          </div>
        </div>

        <button hidden={!(uploadedState == 3 || uploadedState == 4)} className="cool-btn w-fit" 
          onClick={handleUpload}>
            {uploadedState == 3 ? 'Save' : 'Saving ...'}
        </button>

        {uploadedState == 5 && 
          <Alert icon={<CheckIcon fontSize="inherit" />} severity="success">
            Government created and file uploaded successfully
          </Alert>
        }
        
      </div>
    </div>
  );
}
