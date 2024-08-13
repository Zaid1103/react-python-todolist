import React, { useEffect, useState } from 'react';
import { Grid, Spinner, Box, Flex, Text } from '@chakra-ui/react';
import TaskCard from './TaskCard';
import { BASE_URL } from '../App';

const UserGrid = ({ tasks, setTasks }) => {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const getTasks = async () => {
      try {
        const res = await fetch(BASE_URL + "/tasks");
        const data = await res.json();

        if (!res.ok) {
          throw new Error(data.error);
        }
        setTasks(data);
      } catch (error) {
        console.error(error);
      } finally {
        setIsLoading(false);
      }
    };
    getTasks();
  }, [setTasks]);

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
        <Spinner size="xl" />
      </Box>
    );
  }

  return (
  <>
    <Grid
        templateColumns={{
          base: "1fr",
          md: "repeat(2, 1fr)",
          lg: "repeat(3, 1fr)"
        }}
        gap={4}
      >
        {tasks.map((task) => (
          <TaskCard key={task.id} task={task} setTasks={setTasks} />
        ))}
      </Grid>

      {isLoading && (
        <Flex justifyContent={"center"}>
          <Spinner size={"x1"} />
        </Flex>
      )}
      {!isLoading && tasks.length == 0 && (
        <Flex justifyContent={"center"}>
          <Text fontSize={"x1"}>
            <Text as={"span"} fontSize={"2x1"} fontWeight={"bold"} mr={2}>
              No Tasks
            </Text>
            Well Done!
          </Text>
        </Flex>
      )}


  </>
    

    
  );
};

export default UserGrid;
