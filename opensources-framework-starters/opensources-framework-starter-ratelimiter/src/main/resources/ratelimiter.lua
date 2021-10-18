--
-- Created by IntelliJ IDEA.
-- User: yuce
-- Date: 2021/2/2
-- Time: 下午5:36
-- To change this template use File | Settings | File Templates.
-- 限流


local limit = tonumber(KEYS[1]);
local interval = tonumber(KEYS[2]);
local limit_key = "reatelimit:"..ARGS[1];

local currentLimit = tonumber(redis.call("GET " ,  limit_key) , "0");

if currentLimit + 1 > limit then
    return false;
else
    redis.call("INCRBY" , limit_key , 1);
    if currentLimit == 0 then
        redis.call("EXPIRE" , limit_key , interval);
    end
    return true;
end





