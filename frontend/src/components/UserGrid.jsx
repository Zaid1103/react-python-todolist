import React from 'react'
import {Grid} from '@chakra-ui/react'
import { TASKS } from '../dummy/dummy'
import TaskCard from './TaskCard'

const UserGrid = () => {
  return (
    <Grid templateColumns={{
      base: "1fr",
      md: "repeat(2, 1fr)",
      lg: "repeat(3, 1fr)"
    }}
    gap={4}
    >
      {TASKS.map((task) => (
        <TaskCard key={task.id} task={task} />
      ))}

    </Grid>
  )
}

export default UserGrid