import { Text, Card, CardHeader, Flex, IconButton, Box, Heading, CardBody } from '@chakra-ui/react'
import React from 'react'
import {BiTrash} from 'react-icons/bi'
import EditModal from './EditModal'

const TaskCard = ({ task }) => {
  return (
    <Card>
      <CardHeader>
        <Flex gap={4}>
          <Flex flex={"1"} gap={"4"} alignItems={"center"}>
            <Box>
              <Heading size='sm'>{task.name}</Heading>
            </Box>
          </Flex>
          <Flex>
            <EditModal />
            <IconButton
              variant='ghost'
              colorScheme='red'
              size={"sm"}
              aria-label='See Menu'
              icon={<BiTrash size={20} />}
              />
          </Flex>
        </Flex>
      </CardHeader>

      <CardBody>
        <Text>
          {task.description}
        </Text>
      </CardBody>
    </Card>

  )
}

export default TaskCard