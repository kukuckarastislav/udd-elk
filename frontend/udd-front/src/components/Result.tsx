import React from 'react'
import { DocumentDTO } from '../models/DocumentDTO';
import { Button } from '@mui/material';

interface ResultProps {
  doc: DocumentDTO;
}

export default function Result({ doc }: ResultProps) {

  const getHighlight = () => {
    let text = ''
    if(doc.typeOfDoc === 'CONTRACT') {
      text = doc.contractText
    }else{
      text = doc.lawText
    }

    let highlight = text.substring(0, 1000);
    if(text.length > 1000) {
      highlight += '...';
    }

    return highlight;
  }


  return (
    <div key={doc.fileId} className='flex flex-col gap-2 mt-4 hover:bg-slate-100 bg-slate-50 rounded-md shadow-sm p-4'>
      <div className='flex justify-between'>
        <div>
          <h2 className='text-md font-bold text-blue-800'>{doc.title}</h2>
          <p className='text-sm text-violet-700'>{doc.typeOfDoc}</p>
        </div>
          <a href={"http://localhost:8080/api/file/"+doc.fileId} download="${result.title.replace(/\s+/g, '-')}">
          <Button variant='outlined' size='small' color='primary' >Download</Button>
          </a>
        
      </div>
      
      {doc.typeOfDoc === 'CONTRACT' && 
        <div className='bg-white rounded-lg p-3 w-fit'>
          
          <p className='text-sm'>Employee: <b>{doc.employeeName} {doc.employerSurname}</b></p>
          <p className='text-sm'>Government name: <b>{doc.governmentName}</b></p>
          <p className='text-sm'>Government level: <b>{doc.governmentLevel}</b></p>
          <p className='text-sm'>Government address: <b>{doc.address}</b></p>
        </div>
      } 


      <div className='mt-4 text-gray-700'>{getHighlight()}</div>
      
    </div>
  )
}
