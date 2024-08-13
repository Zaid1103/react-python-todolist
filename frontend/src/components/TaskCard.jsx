import { Text, Card, CardHeader, Flex, IconButton, Box, Heading, CardBody, useToast } from '@chakra-ui/react'
import React from 'react'
import { TiTickOutline } from "react-icons/ti";
import EditModal from './EditModal'
import { BASE_URL } from '../App'

const TaskCard = ({ task, setTasks }) => {
  const toast = useToast()
  const handleDeleteUser = async () => {
    try {
      const res = await fetch(BASE_URL + "/tasks/" + task.id, {
        method: "DELETE",
      })
      const data = await res.json()
      if(!res.ok) {
        throw new Error(data.error)
      }
      setTasks((prevTasks) => prevTasks.filter((t) => t.id !== task.id))
      toast({
        title: 'Task deleted',
        description: "We've removed your task for you.",
        status: 'success',
        duration: 9000,
        isClosable: true,
      });
    } catch (error) {
      toast({
        title: "an error occurred",
        description: error.message, 
        status: "error",
        duration: 4000,
      })
      
    }
  }

  return (
    <Card>
      <CardHeader>
        <Flex gap={4}>
          <Flex flex={"1"} gap={"4"} alignItems={"center"}>
            <Box>
              <Heading size='sm' fontSize={25} >{task.name}</Heading>
            </Box>
          </Flex>
          <Flex>
            <EditModal 
               setTasks={setTasks} task={task}
               />
            <IconButton
              variant='ghost'
              colorScheme='green'
              size={"sm"}
              aria-label='See Menu'
              icon={<TiTickOutline size={25} />}
              onClick={handleDeleteUser}
              />
          </Flex>
        </Flex>
      </CardHeader>

      <CardBody>
        <Text fontSize={15}>
          {task.description}
        </Text>
      </CardBody>
    </Card>

  )
}

export default TaskCard